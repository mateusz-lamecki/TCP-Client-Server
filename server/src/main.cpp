#include <iostream>
#include "serverservice.h"

int main(int argc, char* argv[]) {
    sk2::ServerService connection(3552);
    connection.init();
    connection.main_loop();

    return(0);
}
