#ifndef SERVER_H
#define SERVER_H

#include "NetworkCommunicator.h"
#include <netinet/in.h>

class Server : public NetworkCommunicator{
    public:
        Server();
        virtual ~Server();
        void run();
    private:
};

#endif // SERVER_H
