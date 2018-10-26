#ifndef SERVER_H_INCLUDED
#define SERVER_H_INCLUDED

#include <netinet/in.h>

class Server{
    public:
        Server();
        void run();
    private:
        struct sockaddr_in server;
        int sock;

};

#endif // SERVER_H_INCLUDED
