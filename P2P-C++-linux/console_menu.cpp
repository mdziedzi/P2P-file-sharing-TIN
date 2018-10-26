#include "console_menu.h"
#include <iostream>
using namespace std;

ConsoleMenu::ConsoleMenu() {}

void ConsoleMenu::run(void){
    while(true){
        cout << "1 - Zapytaj o wezly w sieci." << endl;
        cout << "exit - wyjscie." << endl;
        string line;
        getline(cin, line);
        if (line == "1"){
            cout << "Zapytanie o wezly w sieci." << endl;
        }
        if (line == "exit"){
            exit(1);
        }

    }
}
