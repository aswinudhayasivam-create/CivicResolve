#include "audit_logger.h"
#include <stdio.h>
#include <time.h>
int audit_log(const char*l,const char*e,const char*d){FILE*f=fopen("audit.log","a");if(!f)return -1;time_t t=time(NULL);fprintf(f,"[%ld] [%s] %s | %s\n",(long)t,l?l:"INFO",e?e:"EVENT",d?d:"");fclose(f);return 0;}