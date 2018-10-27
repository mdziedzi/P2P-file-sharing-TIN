#include "Controller.h"
#include "Serializator.h"
#include "Client.h"
#include <iostream>

using namespace std;

Controller::Controller()
{
    //ctor
}

Controller::~Controller()
{
    //dtor
}

vector <Node> Controller::get_active_nodes(){
    vector <struct sockaddr_in> a = client.get_active_nodes();
    Serializator s;
    return s.get_nodes(a);
}
