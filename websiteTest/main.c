#include <stdio.h>

int main(int argc, char *argv[]) {
    int x = 1;
    for (int i = 1; i <= 128; i += i) {
    x += x;
}
printf("x = %d\n", x);

    return 0;
}