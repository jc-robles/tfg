import numpy as np
from scipy import signal
from sklearn.decomposition import PCA
    
def two_min_walk (gyrx1, gyry1, gyrz1, Fs):

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
  
    ti_im = []
    ti_p = []
    for i in range(len(peaks)):
      if (i==0):
        ti_im = np.append(ti_im, peaks[i])
      elif (i%2 == 0):
        ti_im = np.append(ti_im, peaks[i]-peaks[i-1])
      else:
        ti_p = np.append(ti_p, peaks[i]-peaks[i-1])
    
    ti_par = np.array(ti_p/100)
    ti_impar = np.array(ti_im/100)
    gait_sym = (np.mean(ti_par)*10) - (np.mean(ti_impar)*10)
    gait_sym = np.around(gait_sym, decimals = 2)
  else:
    numSteps = 0
    gait_sym = 0
  
  return numSteps, gait_sym

# Execution Example
Fs=100
np.set_printoptions(threshold=np.inf, linewidth=np.nan)
df=np.array(pd.read_csv(sys.argv[1], sep=";"))
gyrx = df[2:,4].astype(float)
gyry = df[2:,5].astype(float)
gyrz = df[2:,6].astype(float)

numSteps, gait_sym = two_min_walk (gyrx, gyry, gyrz, Fs)
result = {
  "numSteps": str(numSteps),
  "gait_sym": str(gait_sym)
}
print(json.dumps(result, indent=4))