#ifndef NETWORKCOMMUNICATOR_H
#define NETWORKCOMMUNICATOR_H

#include <netinet/in.h>

class NetworkCommunicator
{
    public:
        NetworkCommunicator(int port);
        virtual ~NetworkCommunicator();

    protected:
        struct sockaddr_in addr;
        int sock;
    private:
};

#endif // NETWORKCOMMUNICATOR_H
