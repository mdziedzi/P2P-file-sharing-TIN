#include "NetworkCommunicator.h"
#include <iostream>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>

NetworkCommunicator::NetworkCommunicator(int port)
{
    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == -1) {
        perror("Error while opening stream socket");
        exit(1);
    }
    int broadcast = 1;
    if(setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) < 0){
        perror("Error while setting option to broadcast.");
        exit(1);
    }
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = INADDR_ANY;
    if (bind(sock, (struct sockaddr *) &addr, sizeof addr)== -1) {
        perror("Error while binding stream socket.");
        exit(1);
    }
    socklen_t length = sizeof(addr);
    if (getsockname(sock,(struct sockaddr *) &addr, &length) == -1) {
        perror("Error while getting socket name");
        exit(1);
    }
}

NetworkCommunicator::~NetworkCommunicator()
{
    //dtor
}
