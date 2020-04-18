#virtual register
Every virtual register is mapped to a physical address in ram.

The value of a virtual register is:

 - int type: 4B in ram, the value of the integer
 - bool type: 1B in ram, the value of boolean
 - string type: 4B in ram, the address of string
 - array type: 4B in ram, the address of array
 - class type: 4B in ram, the address of class object

