#include "Client.h"
#include "../consts.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <iostream>
#include <cstring>
#include <sys/time.h>
#include <unistd.h>

Client::~Client()
{
    close(sock);
}

using namespace std;

Client::Client() :NetworkCommunicator(CLIENT_PORT){
    broadcast_addr.sin_family = AF_INET;
    broadcast_addr.sin_port = htons(SERVER_PORT);
    broadcast_addr.sin_addr.s_addr = INADDR_BROADCAST;
}

vector <struct sockaddr_in> Client::get_active_nodes(){
    vector<struct sockaddr_in> active_addresses;
    send_presence_request();
    active_addresses = wait_for_presence_response();
    return active_addresses;
}

void Client::send_presence_request(){
    int message[1] = { 100, };
    ssize_t slen = sizeof(broadcast_addr);
    if (sendto(sock, &message, sizeof(message), 0, (struct sockaddr*) &broadcast_addr, slen) == -1){
        perror("Error while sending");
        exit(1);
    }
}

vector <struct sockaddr_in> Client::wait_for_presence_response(){
    vector<struct sockaddr_in> active_addresses;
    struct timeval tv;
    tv.tv_sec = 2;
    tv.tv_usec = 0;
    fd_set fds;
    FD_ZERO(&fds);
    FD_SET(sock, &fds);
    int buffor;
    while(select(sock+1, &fds, NULL, NULL, &tv) > 0){
        struct sockaddr_in src_addr;
        socklen_t src_size = sizeof(src_addr);
        if(recvfrom(sock, &buffor, sizeof(buffor), 0, (struct sockaddr*) &src_addr, &src_size) == -1){
            perror("Error reveiving sending");
            exit(1);
        }else{
            active_addresses.push_back(src_addr);
        }
        tv.tv_sec = 1;
    }
    return active_addresses;
}


