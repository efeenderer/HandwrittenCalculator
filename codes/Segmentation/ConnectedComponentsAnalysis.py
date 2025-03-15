import cv2
import numpy as np
import os

images_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"

for images in os.listdir(images_path):
    
    if images.split(".")[-1] in ["png","jpg","jpeg","webp"]:
        
        image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\ParsingTests"+f"\\{images}")

        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        blur = cv2.GaussianBlur(gray, (5,5), 0)
        
        binary = cv2.adaptiveThreshold(blur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, 
                                    cv2.THRESH_BINARY_INV, 15, 4)
        num_labels, labels, stats, centroids = cv2.connectedComponentsWithStats(binary, 4, cv2.CV_32S)

        for i in range(1, num_labels):  # Ignore background (label 0)
            x, y, w, h, area = stats[i]
            cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
    
        # Filter out small noise (adjust threshold as needed)
        if area > 20:  
            cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

        # Show the result
        cv2.imshow("Segmented Text", image)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

