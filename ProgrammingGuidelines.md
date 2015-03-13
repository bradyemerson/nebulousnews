# Programming Guidelines #

The purpose of this page is to set the general guidelines on the syntax and naming conventions that will be used in this project along with the coding structure.  Any modifications to this document must be approved by a quorum of the lead developers on this project.


# Variables, Objects, and Methods Conventions #

The camel case convention will be used for dynamic variables, objects and methods.  This means the first letter of an identifier is lowercase, and the first letter of each subsequent concatenated word is capitalized.  Constant literals and identifiers that consist of two or fewer letters will be uppercased.

## SVN Test Data Sets ##

When data sets are stored on the SVN the size should not exceed 5 MB's for that particular domain.  If the data set is going to be larger than 5 MB's please only store locally on the test machine or on your personal directory in HDFS.