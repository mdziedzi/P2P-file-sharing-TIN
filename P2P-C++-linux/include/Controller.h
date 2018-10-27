#ifndef CONTROLLER_H
#define CONTROLLER_H
#include <vector>
#include "Node.h"
#include "Client.h"

using namespace std;

class Controller
{
    public:
        Controller();
        virtual ~Controller();
        vector <Node> get_active_nodes();

    protected:

    private:
        Client client;
};

#endif // CONTROLLER_H
