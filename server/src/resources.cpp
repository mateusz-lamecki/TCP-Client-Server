#include "resources.h"


namespace sk2 {

namespace resources {

User::User(std::string login, std::string password="") : login(login), password(password) { }

std::string User::generate_token() const {
    // TODO: Implement hasing
    return login;
}

bool User::pass_matches(std::string password) const {
    return password == this->password;
}

bool User::operator ==(const User &rhs) const {
    return login == rhs.login;
}

bool User::operator <(const User &rhs) const {
    return login < rhs.login;
}


Resources::Resources() {
    users.insert(User("mateusz","pass"));
}

bool Resources::register_user(std::string login, std::string password) {
    auto it = users.find(User(login));
    if(it != users.end()) return false;

    users.insert(User(login, password));
    return true;
}


std::string Resources::get_user_token(std::string login, std::string password) {
    auto it = users.find(User(login));
    if(it != users.end() && it->pass_matches(password)) {
        return it->generate_token();
    } else {
        return NON_EXISTING_TOKEN;
    }
}



} // namespace resources

} // namespace sk2
