#ifndef SERIALIZATOR_H
#define SERIALIZATOR_H
#include <vector>
#include "Node.h"

using namespace std;

class Serializator
{
    public:
        Serializator();
        virtual ~Serializator();
        vector <Node> get_nodes(vector <struct sockaddr_in>);

    protected:

    private:
};

#endif // SERIALIZATOR_H
