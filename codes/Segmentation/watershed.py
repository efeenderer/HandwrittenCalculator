import cv2
import numpy as np
import os

images_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"

for images in os.listdir(images_path):
    
    if images.split(".")[-1] in ["png","jpg","jpeg","webp"]:
        
        image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"+f"\\{images}")

