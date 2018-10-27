#ifndef SERVER_H
#define SERVER_H

#include <netinet/in.h>

class Server{
    public:
        Server();
        virtual ~Server();
        void run();
    private:
        struct sockaddr_in server;
        int sock;

};

#endif // SERVER_H
