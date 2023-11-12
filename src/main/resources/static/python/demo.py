from iapws import IAPWS97
import sys

def method1(t,p):
 print(type(t))
 water=IAPWS97(T=float(t)+273.15,P=float(p))
 return water.h

print(method1(sys.argv[1],sys.argv[2]))