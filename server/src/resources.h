#pragma once

#include <set>
#include <vector>
#include <string>


namespace sk2 {

namespace resources {

class Topic {
    std::string name;
    std::vector<std::string> description;
};

class User {
    public:
    User(std::string login, std::string password);
    std::string generate_token() const;
    bool operator ==(const User &rhs) const;
    bool operator <(const User &rhs) const;

    private:
    std::string login;
    std::string password;
    std::string token;
};

class Resources {
    public:
    Resources();
    std::string get_user_token(std::string login, std::string password);
    const std::string NON_EXISTING_TOKEN = "KDKFMF-NON-EXISTING-ASDASD";

    private:
    std::set<Topic> topic; // TODO: change to unordered_set
    std::set<User> users; // TODO: change to unordered_set
};

} // namespace resources

} // namespace sk2
