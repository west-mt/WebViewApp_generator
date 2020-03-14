#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import getopt
import re
import os
import shutil


def replace_copy(src, dst, old, new):
    src_data = open(src, 'rb').read()
    dst_data = src_data.replace(old, new)
    of = open(dst, 'wb')
    of.write(dst_data)
    of.close

try:
    opts, args = getopt.getopt(sys.argv[1:], "-l:p:")
except getopt.GetoptError:
    print 'Usage: %s [-l api_level] [-p package_name] application_name\n' % sys.argv[0]
    sys.exit(2)

aname_patt = re.compile(r'[A-Za-z_][A-Za-z0-9_]*$')

pname = ''
aname = ''
alevel = 8
dpath = os.path.dirname(os.path.realpath(__file__))

if len(args) != 1:
    print 'Usage: %s [-l api_level] [-p package_name] application_name\n' % sys.argv[0]
    sys.exit(1)


if not aname_patt.match(args[0]):
    print 'Usage: %s [-l api_level] [-p package_name] application_name' % sys.argv[0]
    print 'ERROR: Application name invalid: %s\n' % args[0]
    sys.exit(2)


aname = args[0]
for o, a in opts:
    if o == '-p':
        pname = a
    if o == '-l':
        alevel = int(a)

if pname == '':
    pname = 'net.sytes.vision.' + aname

for p in pname.split('.'):
    if not aname_patt.match(p):
        print 'Usage: %s [-l api_level] [-p package_name] application_name' % sys.argv[0]
        print 'ERROR: Package name invalid: %s\n' % pname
        sys.exit(2)
        

print 'package: %s, application: %s' % (pname, aname)

if os.path.exists(aname):
    print 'ERROR: File or directory exists: %s\n' % aname
    sys.exit(2)
    
cmd = 'android create project -t android-%d -n %s -p %s -k %s -a WViewApp' % (alevel, aname, aname, pname)
print cmd
os.system(cmd)

    

ppath = os.sep.join(pname.split('.'))
old_pname = 'net.sytes.vision.WViewApp'
old_aname = 'WViewApp'

for f in ['AndroidManifest.xml', 'ant.properties']:
    replace_copy(os.path.join(dpath, f), os.path.join(aname, f),
                 old_pname, pname)

replace_copy(os.path.join(aname, 'res/values/strings.xml'),
             os.path.join(aname, 'res/values/strings.xml'),
             old_aname, aname)

for s in ['WViewApp.java', 'WViewBridge.java']:
    replace_copy(os.path.join(dpath, s),
                 os.path.join(aname, 'src', ppath, s),
                 old_pname, pname)

for i in ['assets', 'res/drawable-hdpi', 'res/drawable-mdpi', 'res/drawable-xhdpi', 'res/drawable-xxhdpi']:
    shutil.copytree(os.path.join(dpath, i),
                    os.path.join(aname, i))

shutil.copy(os.path.join(dpath, 'http_server.py'), aname)
