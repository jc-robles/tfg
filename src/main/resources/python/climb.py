import numpy as np
from scipy import signal
from sklearn.decomposition import PCA
    
def climbing_stairs (gyrx1, gyry1, gyrz1, Fs):

  gyrx = np.array(gyrx1)
  gyrx = gyrx.reshape(len(gyrx),1)
  gyry = np.array(gyry1)
  gyry = gyry.reshape(len(gyry),1)
  gyrz = np.array(gyrz1)
  gyrz = gyrz.reshape(len(gyrz),1)
  
  
  gyr = np.array(gyrx)
  gyr = np.append(gyr, gyry, axis=1)
  gyr = np.append(gyr, gyrz, axis=1)
  
  pca = PCA(n_components=3, svd_solver='full')
  pca.fit(gyr)
  PCA_gyr = pca.transform(gyr)
  
  gyry = PCA_gyr[:,0]
  gyr_p = np.array(gyry)
  gyr_i = gyr_p.flatten()
  gyr_i = signal.medfilt(gyr_i, 13);
  l_gyr = len(gyr_i)
  gyr_i = gyr_i - np.mean(gyr_i)
  minPeakHeight = np.std(gyr_i[(Fs*2):(l_gyr-(Fs*2))]) * 0.7;
  peaks1, properties = signal.find_peaks(gyr_i, height=minPeakHeight, distance=Fs/2)

  gyr_p = -np.array(gyr_i)
  minPeakHeight = np.std(gyr_p[(Fs*2):(l_gyr-(Fs*2))]) * 0.7;
  peaks2, properties = signal.find_peaks(gyr_p, height=minPeakHeight, distance=Fs/2)
  
  if peaks1.size and peaks2.size:
  
    peaks = np.sort(np.append(peaks1, peaks2))
    numSteps = len(peaks)
    
    # Step regularity
    cofcorr = []
    for i in range(len(peaks)):  
      if i==0:
        corr1=gyr_i[0:peaks[i]]
        corr2=gyr_i[peaks[len(peaks)-2]:peaks[len(peaks)-1]]
      elif i==len(peaks)-1:
        corr1=gyr_i[peaks[i-1]:peaks[i]]
        corr2=gyr_i[1:peaks[0]]
      else:
        corr1=gyr_i[peaks[i-1]:peaks[i]]
        corr2=gyr_i[peaks[i]:peaks[i+1]]
    
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
    
    # Gait Simetry
    stri = numSteps//2
    
    s=0
    st_cofcorr=[]
    while s<=len(peaks)-1:   
      if s == 0:
        corr1=gyr_i[0:peaks[1]]
        corr2=gyr_i[peaks[len(peaks)-3]:peaks[len(peaks)-1]]
      elif s == len(peaks)-1:
        corr1=gyr_i[peaks[s-2]:peaks[s]]
        corr2=gyr_i[0:peaks[1]]
      elif s==len(peaks)-2:
        corr1=gyr_i[peaks[s-2]:peaks[s]]
        corr2=gyr_i[0:peaks[1]]
      else:
        corr1=gyr_i[peaks[s-2]:peaks[s]]
        corr2=gyr_i[peaks[s]:peaks[s+2]]
     
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
    gait_sym = np.around(gait_sym, decimals = 1)
  else:
    numSteps = 0
    gait_sym = 0  
  
  return numSteps, gait_sym
  
  


