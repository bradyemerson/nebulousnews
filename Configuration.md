# A brief outline of how configuration of tasks is performed.

# Introduction #

Since the Nebulous News has to be configurable on a site-to-site basis.  There has to be a way to convey that configuration to Nebulous News.  This wiki entry describes the method that configuration is conveyed

# Details #

Each configuration file is a simple .conf file.  Each line contains options in the format of option=value. A line can be defined as a comment line by adding a cross-hatch '#' at the start of the line.  Configuration files do not acknowledge white space (except \n).  For this reason, a variable cannot contain whitespace, or begin with a '#'.
Any value read in after a pre-existing value of the same key will overwrite the pre-existing value.
```
#sample configuration

conf-value1 = foo #set conf-value1 to foo
conf-value1 = bar #will overwrite conf-value1 to bar
conf2       = #will set conf-value2 to an empty string
var1        = foo #will set var1 to foo
```
Every new instance of ConfigReader will read in the default and site configuration files by default.  It is possible to read in other configuration files by running readConfigFile(File path), which will read in all the configuration values from the new file.

# Retrieval #

Every time configuration values are needed, call an instance of the ConfigReader, if one hasn't already been called in the context in question.  Call getOption(String key) for each option you wish to get.  All options are saved as strings, so they may need to be parsed after retrieval.

## Underlying Thinking ##

This way of processing configuration isn't terribly clean or efficient, but the thinking behind it is that it allows users to generate their own configurations for their own plugins and know that they can be accessed without much added overhead.