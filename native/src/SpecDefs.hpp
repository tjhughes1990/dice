/**
 * @file SpecDefs header file.
 */

/**
 * @brief Define a macro to ignore unused variables for the G++ compiler. This is required to compile without
 * warnings, as the JNI generated header includes an unused argument in the method signature for performRolls().
 */
#if __GNUG__
    #define UNUSED __attribute__((unused))
#else
    #define UNUSED ""
#endif

