import os
import json
import sys


import math as mt
import numpy as np
import pandas as pd
import scipy.io
from scipy import signal, integrate
from scipy.fft import fft, fftfreq
from sklearn import preprocessing
import matplotlib.pyplot as plt
from scipy.interpolate import interp1d


def Invert_axis(lst):
    return [ -i for i in lst ]

def remove_fase_positives (step_time, peaks):
  Cadence = np.mean(step_time)
  False_Positives = [0]
  for i in range(len(step_time)-1):
    if (step_time[i] < (Cadence*0.3)):
      if i != len(step_time)-1:
        if step_time[i+1] <= (Cadence*1.5) or step_time[i-1] <= (Cadence*1.5):
          False_Positives = np.append(False_Positives, i)
  False_Positives = np.delete(False_Positives, 0)
  peaks = np.delete(peaks, False_Positives)
  return peaks

def calculate_step_time (peaks):
  ti = []
  for i in range(len(peaks)):
    if (i==0):
      ti = np.append(ti, peaks[i])
    else:
      ti = np.append(ti, peaks[i]-peaks[i-1])
  return ti

def calculate_step_Length (acc_fy, peaks, leg_length):
  Step_Lenght= []
  acc_1Int = integrate.cumtrapz(acc_fy, axis = 0)
  b,a = signal.butter(4, 0.1/(Fs/2), 'high')
  acc_1Int = signal.lfilter(b, a, acc_1Int, axis=0)
  acc_2Int = integrate.cumtrapz(acc_1Int, axis = 0)
  acc_2Int = signal.lfilter(b, a, acc_2Int, axis=0)
  for i in range(len(peaks)-1):
    AmpMax = max(acc_2Int[peaks[i]:peaks[i+1]])
    AmpMin = min(acc_2Int[peaks[i]:peaks[i+1]])
    Amp = AmpMax-AmpMin
    Stepl = 2*np.sqrt((2*leg_length*Amp)-(Amp**2))
    Step_Lenght = np.append(Step_Lenght, Stepl)
  return Step_Lenght

def HarmoicRatio(sensordata, locs_peaks, Fsamp, Is_AP):
  locHR=locs_peaks
  if len(locHR) % 2 == 0:
    locHR = np.delete(locHR, [0,1,len(locHR)-2,len(locHR)-1]) #remove the first two steps and the last two steps
  else:
    locHR = np.delete(locHR, [0,1,len(locHR)-1])              #remove the first two steps and the last step

  L = mt.floor(len(locHR)/2)
  HR = np.zeros(L)
  for i in range(L):
    odd_peaks = 0
    data_temp = sensordata[locHR[i*2]-mt.floor(Fsamp*0.2):locHR[i*2+1]+mt.floor(Fsamp*0.2)]
    acc_fft = fft(data_temp)
    NSamp = len(data_temp) # Sample point
    T = 1/Fsamp # sample spacing
    Espc = 2.0/NSamp * np.abs(acc_fft[0:NSamp//2])
    Espc = Espc.flatten()
    # find harmonics
    peaks, properties = signal.find_peaks(Espc)
    Hev = 0
    Hod = 0
    len_pks = len(peaks)
    if len_pks > 20: # only first 20 harmonics
      peaks = peaks[0:19]

    if len_pks%2 ==  1: # odd number of harmonics
      odd_peaks = 1

    for s in range(mt.floor(len(peaks)/2)):
      Hod = Hod+ Espc[peaks[s*2]]
      Hev = Hev+ Espc[peaks[s*2+1]]

    if odd_peaks == 1:
      Hod = Hod+Espc[peaks[len(peaks)-1]]

    if Is_AP == 1:            # Anterior-posterior direction
      HR[i] = Hev / Hod
    else:                     # Medio-lateral direction
      HR[i] = Hod / Hev
  return HR


def two_min_walk (accx, accy, accz, Fs):
  T = 1/Fs
  order=4;
  fc = 10;
  b,a = signal.butter(4, fc/(Fs/2), 'low')
  acc_fx = signal.lfilter(b, a, accx, axis=0)
  acc_fy = signal.lfilter(b, a, accy, axis=0)
  acc_fz = signal.lfilter(b, a, accz, axis=0)
  acc_fft = np.fft.fft(acc_fz, axis=0)
  L=len(acc_fft);
  arr = np.arange(L//2)
  freq = Fs*arr/L
  Esp2 = np.abs(acc_fft/L);
  Espc1 = Esp2[0:(L//2)];
  end=len(Espc1)-1;
  Espc1[2:end-1] = 2*Espc1[2:end-1]
  Fm = max(Espc1[20:((L//2)*6//50)])*0.5
  Freq1D = Espc1.flatten()
  peaks1, properties = signal.find_peaks(Freq1D[20:((L//2)*6//50)], height=Fm)

  Fmin= freq[peaks1[0]+19];
  Fmax= freq[peaks1[len(peaks1)-1]+19];

  if Fmin < 0.2:
    Fmin = 0.2
  elif Fmin > 0.5:
    Fmin = 0.5
  if Fmax < 2:
    Fmax = 2

  b,a = signal.butter(4, [Fmin/(Fs/2), Fmax/(Fs/2),], 'bandpass')
  acc_ffx = signal.lfilter(b, a, acc_fx, axis=0)
  acc_ffy = signal.lfilter(b, a, acc_fy, axis=0)
  acc_ffz = signal.lfilter(b, a, acc_fz, axis=0)
  acc_p=np.sqrt(acc_ffx**2+acc_ffy**2+acc_ffz**2)
  acc_i = acc_p.flatten()
  acc_i = signal.medfilt(acc_i, 49);
  # Apply a high pass filter to detrend the signal
  b,a = signal.butter(2, 0.1/(Fs/2), 'high')
  acc_pp = signal.lfilter(b, a, acc_i, axis=0)
  l_acc = len(acc_i)

  acc_i= -acc_pp;
  minPeakHeight = np.std(acc_i[200:(l_acc-200)]) * 0.40;
  acc_i = signal.medfilt(acc_i, 9);
  peaks1, properties = signal.find_peaks(acc_i, height=minPeakHeight, distance=30)

  acc_i= acc_pp;
  minPeakHeight = np.std(acc_i[200:(l_acc-200)]) * 0.40;
  acc_i = signal.medfilt(acc_i, 9);

  peaks2 = []
  count = 0
  while ( np.abs(len(peaks2) - len(peaks1)) >= 4 ):
    peaks2, properties2 = signal.find_peaks(acc_i, height=minPeakHeight, distance=30)
    if len(peaks2) > len(peaks1):
      minPeakHeight= minPeakHeight + 0.001;
    else:
      minPeakHeight= minPeakHeight - 0.001;
    if count > 200:
      break
    else:
      count += 1
  numSteps = len(peaks2) + len(peaks1)
  peaks = np.sort(np.append(peaks1, peaks2))

  # Mean Step Time

  # Step time
  ti = calculate_step_time(peaks)

  # Remove False Positives
  peaks = remove_fase_positives (ti, peaks)

  #  Recalculate times
  ti = calculate_step_time(peaks)

  for i in range(len(ti)):
    ti[i]=ti[i]/Fs

  numSteps = len(peaks)
  M_st_tm = sum(ti)/numSteps;

  # Cadence
  Cadence=1/M_st_tm;

  # Step time variability
  St_tv=np.std(ti);

  # Step regularity
  cofcorr = []
  for i in range(len(peaks)):
    if i==0:
      corr1=acc_i[0:peaks[i]]
      corr2=acc_i[peaks[len(peaks)-2]:peaks[len(peaks)-1]]
    elif i==len(peaks)-1:
      corr1=acc_i[peaks[i-1]:peaks[i]]
      corr2=acc_i[1:peaks[0]]
    else:
      corr1=acc_i[peaks[i-1]:peaks[i]]
      corr2=acc_i[peaks[i]:peaks[i+1]]

    if len(corr1)>len(corr2):
      N = len(corr1) - len(corr2)
      corr2 = np.pad(corr2, (0, N), 'constant')
    elif len(corr1)<len(corr2):
      N = len(corr2) - len(corr1)
      corr1 = np.pad(corr1, (0, N), 'constant')

    cofc = np.corrcoef(corr1, corr2, rowvar=False)
    cofcorr = np.append(cofcorr, min(cofc[1]))

  sum_cofcrr=sum(np.abs(cofcorr));

  step_reg = sum_cofcrr/numSteps;

  # Step regularity odd
  s=1
  cofcorr_odd=[]
  while s<=len(peaks)-1:
    if s==0:
      corr1=acc_i[0:peaks(s)]
      corr2=acc_i[peaks[s+1]:peaks[s+2]]
    elif s==len(peaks)-1:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[0:peaks[0]]
    elif s==len(peaks)-2:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[0:peaks[0]]
    else:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[peaks[s+1]:peaks[s+2]]


    if len(corr1)>len(corr2):
      N = len(corr1) - len(corr2)
      corr2 = np.pad(corr2, (0, N), 'constant')
    elif len(corr1)<len(corr2):
      N = len(corr2) - len(corr1)
      corr1 = np.pad(corr1, (0, N), 'constant')

    cofc = np.corrcoef(corr1, corr2, rowvar=False)
    cofcorr_odd = np.append(cofcorr, min(cofc[1]))
    if s==len(peaks)-2:
      s=s+1
    else:
      s=s+2;

  sum_cofcrr_odd=sum(np.abs(cofcorr_odd));
  step_reg_odd = sum_cofcrr_odd/(numSteps/2);

  # Step regularity even
  s=2
  cofcorr_odd=[]
  while s<=len(peaks)-1:
    if s==len(peaks)-1:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[0:peaks[0]]
    elif s==len(peaks)-2:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[0:peaks[0]]
    else:
      corr1=acc_i[peaks[s-1]:peaks[s]]
      corr2=acc_i[peaks[s+1]:peaks[s+2]]


    if len(corr1)>len(corr2):
      N = len(corr1) - len(corr2)
      corr2 = np.pad(corr2, (0, N), 'constant')
    elif len(corr1)<len(corr2):
      N = len(corr2) - len(corr1)
      corr1 = np.pad(corr1, (0, N), 'constant')

    cofc = np.corrcoef(corr1, corr2, rowvar=False)
    cofcorr_even = np.append(cofcorr, min(cofc[1]))
    s=s+2;

  sum_cofcrr_even=sum(np.abs(cofcorr_even));
  step_reg_even = sum_cofcrr_even/(numSteps/2)

  # Gait Simetry
  stri = numSteps//2

  s=0
  st_cofcorr=[]
  while s<=len(peaks)-1:
    if s == 0:
      corr1=acc_i[0:peaks[1]]
      corr2=acc_i[peaks[len(peaks)-3]:peaks[len(peaks)-1]]
    elif s == len(peaks)-1:
      corr1=acc_i[peaks[s-2]:peaks[s]]
      corr2=acc_i[0:peaks[1]]
    elif s==len(peaks)-2:
      corr1=acc_i[peaks[s-2]:peaks[s]]
      corr2=acc_i[0:peaks[1]]
    else:
      corr1=acc_i[peaks[s-2]:peaks[s]]
      corr2=acc_i[peaks[s]:peaks[s+2]]

    if len(corr1)>len(corr2):
      N = len(corr1) - len(corr2)
      corr2 = np.pad(corr2, (0, N), 'constant')
    elif len(corr1)<len(corr2):
      N = len(corr2) - len(corr1)
      corr1 = np.pad(corr1, (0, N), 'constant')

    cofc = np.corrcoef(corr1, corr2, rowvar=False)
    st_cofcorr = np.append(st_cofcorr, min(cofc[1]))
    s=s+2;

  st_sum_cofcrr=sum(np.abs(st_cofcorr));
  str_reg = st_sum_cofcrr/stri;

  if (str_reg >= step_reg):
    gait_sym = step_reg/str_reg;
  else:
    gait_sym = str_reg/step_reg;

  # Jerk swayness
  dxdt = np.diff(acc_ffx, axis=0);
  dzdt = np.diff(acc_ffz, axis=0);

  derv=[]
  for i in range(len(dxdt)):
    derv = np.append(derv, (dxdt[i]**2) + (dzdt[i]**2))

  JerkSw = (np.trapz(derv))/2

  # Harmonic Ratio
  HR_AP = HarmoicRatio(accx, peaks, Fs, 1) #AnteroPosterior
  HR_ML =HarmoicRatio(accz, peaks, Fs, 0) #Medio Lateral

  return JerkSw, gait_sym, step_reg_even, step_reg_odd, step_reg, St_tv,  Cadence, numSteps, HR_AP, HR_ML



# Execution Example
Fs=100
df=np.array(pd.read_csv('src/main/resources/python/walk3.csv', decimal=","))
accx = df[0:,0].astype(float)
accy = df[0:,1].astype(float)
accz = df[0:,2].astype(float)
JerkSw, gait_sym, step_reg_even, step_reg_odd, step_reg, St_tv,  Cadence, numSteps, HR_AP, HR_ML = two_min_walk (accx, accy, accz, Fs)
#print(two_min_walk (accx, accy, accz, Fs))
#print(JerkSw)
#print(gait_sym)
#print(step_reg_even)
#print(step_reg_odd)
#print(step_reg)
#print(St_tv)
#print(Cadence)
#print(numSteps)
#print(HR_AP)
#print(HR_ML)

dictionary = {
    "name": "sathiyajith",
    "rollno": 56,
    "cgpa": 8.6,
    "phonenumber": "9976770500"
}

print(sys.argv[1])

# Serializing json
print(json.dumps(dictionary, indent=4))
