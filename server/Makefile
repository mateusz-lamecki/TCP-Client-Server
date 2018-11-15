CXX := g++
CXXFLAGS := -Wall -std=c++14 -g
LDFLAGS := -pthread -I include

SRC_DIR = src
BIN_DIR = bin

SRC := $(wildcard ${SRC_DIR}/*.cpp)
OBJ  := $(SRC:.cpp=.o)
EXEC = server


all: $(EXEC)

$(EXEC): $(OBJ)
	$(CXX) $(OBJ) -o $@ $(CXXFLAGS) $(LDFLAGS)

%.o: %.cpp
	$(CXX) -c $(CXXFLAGS) $(LDFLAGS) $< -o $@

clean:
	rm -rf ${OBJ} ${EXEC}
