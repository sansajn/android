#pragma once
#include "platform_macros.h"
#include "config.h"

void _debug_log_v(char const * tag, char const * text, ...) PRINTF_ATTRIBUTE(2, 3);
void _debug_log_d(char const * tag, char const * text, ...) PRINTF_ATTRIBUTE(2, 3);
void _debug_log_w(char const * tag, char const * text, ...) PRINTF_ATTRIBUTE(2, 3);
void _debug_log_e(char const * tag, char const * text, ...) PRINTF_ATTRIBUTE(2, 3);

#define DEBUG_LOG_PRINT_V(tag, fmt, ...) do { if (LOGGING_ON) _debug_log_v(tag, "%s:%d:%s(): " fmt, __FILE__, __LINE__, __func__, __VA_ARGS__); } while (0)
#define DEBUG_LOG_PRINT_D(tag, fmt, ...) do { if (LOGGING_ON) _debug_log_d(tag, "%s:%d:%s(): " fmt, __FILE__, __LINE__, __func__, __VA_ARGS__); } while (0)
#define DEBUG_LOG_PRINT_W(tag, fmt, ...) do { if (LOGGING_ON) _debug_log_w(tag, "%s:%d:%s(): " fmt, __FILE__, __LINE__, __func__, __VA_ARGS__); } while (0)
#define DEBUG_LOG_PRINT_E(tag, fmt, ...) do { if (LOGGING_ON) _debug_log_e(tag, "%s:%d:%s(): " fmt, __FILE__, __LINE__, __func__, __VA_ARGS__); } while (0)

#define DEBUG_LOG_WRITE_V(tag, text) DEBUG_LOG_PRINT_V(tag, "%s", text)
#define DEBUG_LOG_WRITE_D(tag, text) DEBUG_LOG_PRINT_D(tag, "%s", text)
#define DEBUG_LOG_WRITE_W(tag, text) DEBUG_LOG_PRINT_W(tag, "%s", text)
#define DEBUG_LOG_WRITE_E(tag, text) DEBUG_LOG_PRINT_E(tag, "%s", text)

#define CRASH(e) DEBUG_LOG_WRITE_E("Assert", #e); __builtin_trap()
