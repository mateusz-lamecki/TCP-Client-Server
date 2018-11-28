#include "resources.h"


namespace sk2 {

namespace resources {

User::User(std::string login, std::string password) : login(login), password(password) { }

std::string User::generate_token() const {
    // TODO: Implement hasing
    return login;
}


bool User::operator ==(const User &rhs) const {
    return login == rhs.login && password == rhs.password;
}

bool User::operator <(const User &rhs) const {
    if(login == rhs.login) {
        return password < rhs.password;
    }
    return login < rhs.login;
}


Resources::Resources() {
    users.insert(User("mateusz","pass"));
}

std::string Resources::get_user_token(std::string login, std::string password) {
    auto it = users.find(User(login, password));
    if(it != users.end()) return it->generate_token();
    else return NON_EXISTING_TOKEN;
}



} // namespace resources

} // namespace sk2
