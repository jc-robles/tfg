import numpy as np
from scipy import signal

def romberg (accx1, accz1, Fs):
  Fs = 50
  Ti = 1/Fs
  order=4;
  fc = 2;
  
  accx = np.array(accx1)
  accx = accx.reshape(len(accx),1)
  accz = np.array(accz1)
  accz = accz.reshape(len(accz),1)
  
  
  b,a = signal.butter(order, fc/(Fs/2), 'high')
  acc_fx = signal.lfilter(b, a, accx, axis=0)
  # acc_fy = signal.lfilter(b, a, accy, axis=0)
  acc_fz = signal.lfilter(b, a, accz, axis=0)
  acc_i=np.array(acc_fx);
  LeW = int(5/Ti);
  P = np.zeros(len(acc_i)-LeW)
  idx=0
  for i in range(LeW, len(acc_i)-1):
    P[idx]=abs(acc_i[i+1]-acc_i[i]);
    idx = idx+1
  t = (len(acc_i)*0.01) - 5;
  NPL_AP = (sum(P))/t;
  
  acc_pi=np.array(acc_fz);
  P2 = np.zeros(len(acc_pi)-LeW)
  idx=0
  for i in range(LeW, len(acc_pi)-1):
    P2[idx]=abs(acc_pi[i+1]-acc_pi[i]);
    idx = idx+1
  NPL_ML = (sum(P2))/t;
  
  
  NPL_ML = np.around(NPL_ML, decimals = 2)
  NPL_AP = np.around(NPL_AP, decimals = 2)
  
  return NPL_ML, NPL_AP
  
  
  