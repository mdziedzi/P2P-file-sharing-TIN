#include "Client.h"
#include "../consts.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <iostream>
#include <cstring>
#include <sys/time.h>

Client::~Client()
{
    //dtor
}

using namespace std;

Client::Client(void){
    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == -1) {
        perror("Error while opening stream socket");
        exit(1);
    }
    int broadcast = 1;
    if(setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) < 0){
        perror("Error while setting option to broadcast");
        exit(1);
    }
    client.sin_family = AF_INET;
    client.sin_port = htons(CLIENT_PORT);
    client.sin_addr.s_addr = INADDR_ANY;
    if (bind(sock, (struct sockaddr *) &client, sizeof client)== -1) {
        perror("Error while binding stream socket.");
        exit(1);
    }
    socklen_t length = sizeof(client);
    if (getsockname(sock,(struct sockaddr *) &client, &length) == -1) {
        perror("Error while getting socket name");
        exit(1);
    }
    remote_server.sin_family = AF_INET;
    remote_server.sin_port = htons(SERVER_PORT);
    remote_server.sin_addr.s_addr = INADDR_ANY;

}

vector <struct sockaddr_in> Client::get_active_nodes(){
    vector<struct sockaddr_in> active_addresses;
    send_presence_request();
    active_addresses = wait_for_presence_response();
    return active_addresses;
}

void Client::send_presence_request(){
    int message[1] = { 100, };
    ssize_t slen = sizeof(remote_server);
    if (sendto(sock, &message, sizeof(message), 0, (struct sockaddr*) &remote_server, slen) == -1){
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
        cout << "Dostalem " << endl;
        tv.tv_sec = 1;
    }
    return active_addresses;
}


