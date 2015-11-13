# libvep

[![Build Status](https://travis-ci.org/maedoc/libvep.svg?branch=master)](https://travis-ci.org/maedoc/libvep)

infrastructure library for patient virtualization

## Use

### MATLAB

#### Install

1) Either add the path to the jar to your static Java path by editing the `javaclasspath.txt`

```matlab
edit(fullfile(prefdir, 'javaclasspath.txt'))
```

and restarting MATLAB. This is less convenient the first time, but subsequent use requires no path
manipulation.

2) Add the path to the jar to your dynamic Java path

```matlab
javaaddpath path/to/vep.jar
```

This has to be done every time you open MATLAB, which is why option 1 is preferable.

#### Usage

A convenient `VEP` class is available to open connection and work with data, e.g.

```matlab

>> vep = VEP('host', 'user', 'pass')
...
vep =
VEP@1fd9d66d
>> p1 = org.vep.models.Patient('m', 'w', java.util.Date)
p1 =
org.vep.models.Patient@2f7113a3

>> t1 = org.vep.models.T1
t1 =
org.vep.models.T1@55c189aa

>> p1_exams = p1.getExams
p1_exams =
[]
>> p1_exams.add(t1)
ans =
     1

>> vep.dao.addPatient(p1)

% disconnect, restart, etc.

>> vep = VEP('host', 'user', 'pass')
>> vep.dao.getPatients.get(0).getFirstName
ans =
m
```

## Tech

This is built with Intellij, JSch, JUnit, Hibernate and PostgreSQL.


