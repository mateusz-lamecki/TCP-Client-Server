#include <iostream>
#include "server_service.h"

int main(int argc, char* argv[]) {
    sk2::ServerService service(3552);
    service.run();

    return(0);
}
