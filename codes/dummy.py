import cv2
import numpy as np

image = cv2.imread(r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\ParsingTests\uc.png")
save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\ParsingTests\Tests"

gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

blur = cv2.GaussianBlur(gray, (5,5), 0)

_, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

char_images = []
for contour in contours:
    x, y, w, h = cv2.boundingRect(contour)
    char = thresh[y:y+h, x:x+w]
    char_images.append(char)


for i, char in enumerate(char_images):
    cv2.imwrite(f"{save_path}\char_{i}.png", char)
