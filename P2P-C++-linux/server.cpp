#include "server.h"
#include <iostream>
#include "consts.h"

#define BUFLEN 512

using namespace std;


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
    char buf[BUFLEN];
    while (true){
        if ((recv_len = recv(sock, buf, BUFLEN, 0)) == -1){
            perror("Error while receiving data");
            exit(1);
        }
        cout << "Greate I have a message!" << endl;
    }
}
