import cv2
import os



path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\main2_splitted"

for subpath in os.listdir(path):
    subpath = os.path.join(path,subpath)
    for char in os.listdir(subpath):
        char_path = os.path.join(subpath,char)

        for image in os.listdir(char_path):
            image_path = os.path.join(char_path,image)
            img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)

            img = cv2.resize(img, (48,48),interpolation=cv2.INTER_NEAREST)

            cv2.imwrite(image_path,img)
        print(f"{subpath}, {char} is done")
