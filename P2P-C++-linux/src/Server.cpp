#include "Server.h"
#include <iostream>
#include "../consts.h"
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include "../consts.h"

using namespace std;
Server::~Server()
{
    close(sock);
}

Server::Server() :NetworkCommunicator(SERVER_PORT)
{

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
