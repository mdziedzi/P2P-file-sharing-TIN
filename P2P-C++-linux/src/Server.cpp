#include "Server.h"
#include <iostream>
#include "../consts.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define BUFLEN 512

using namespace std;
Server::~Server()
{
    //dtor
}


Server::Server() {
    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == -1) {
        perror("Error while opening stream socket");
        exit(1);
    }
    int broadcast = 1;
    if(setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) < 0){
        perror("Error while setting option to broadcast 1");
        exit(1);
    }
    server.sin_family = AF_INET;
    server.sin_port = htons(SERVER_PORT);
    server.sin_addr.s_addr = INADDR_ANY;
    if (bind(sock, (struct sockaddr *) &server, sizeof server)== -1) {
        perror("Error while binding stream socket 2");
        exit(1);
    }
    socklen_t length = sizeof(server);
    if (getsockname(sock,(struct sockaddr *) &server, &length) == -1) {
        perror("Error while getting socket name");
        exit(1);
    }
    cout << "Listinig UDP socker port: " << ntohs(server.sin_port) << endl;
}

void Server::run(void) {
    ssize_t recv_len;
    int buffor;
    struct sockaddr_in src_addr;
    socklen_t src_size = sizeof(src_addr);
    while (true){
        if ((recv_len = recvfrom(sock, &buffor, sizeof(buffor), 0,  (struct sockaddr *) &src_addr,  &src_size)) == -1){
            perror("Error while receiving data");
            exit(1);
        }
        switch(buffor){
            case PRESENCE:
                cout << "----------Serwer------------" << endl;
                cout << "Port z ktorego dostalem: " << ntohs(src_addr.sin_port) << endl;
                cout << "Adres z ktorego dostalem: "<< inet_ntoa(src_addr.sin_addr) << endl;
                cout << "----------Serwer------------" << endl;
                int message = PRESENCE;
                socklen_t slen = sizeof(src_addr);
                if (sendto(sock, &message, sizeof(message), 0, (struct sockaddr*) &src_addr, slen) == -1){
                    perror("Error while sending");
                    exit(1);
                }
            break;
        }
    }
}
