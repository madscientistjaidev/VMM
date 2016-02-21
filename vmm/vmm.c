#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef enum boolean{true, false};

typedef struct page
{
    int pID;
    char data[256];
};

typedef struct frame
{
    int frameNo;
};

frame phyMem[256];
int tlb[16][2];
page pgT[256];  

int main(int argc, char** argv)
{
    const int mode = argc;
    const char *inPath = argv[0];
    const char *outPath = argv[1];
    
    if(FILE *fopen(inPath, "r")==NULL)
        printf("Unable to open input file.");
    
    if(FILE *fopen(outPath, "w")==NULL)
        printf("Unable to create output file.");
    
    return (EXIT_SUCCESS);
}