import cv2
import os

import numpy as np

letter_dict = {
    '0001': 'a', '0002': 'b', '0003': 'c', '0004': 'd', '0005': 'e', '0006': 'f',
    '0007': 'h', '0008': 'vertical_line', '0009': 'j', '0010': 'k', '0011': 'vertical_line',
    '0012': 'm', '0013': 'n', '0014': 'o', '0015': 'p', '0016': 'q', '0017': 'r',
    '0018': 's', '0019': 't', '0020': 'u', '0021': 'v', '0022': 'w', '0023': 'x',
    '0024': 'y', '0025': 'z', '0026': '0', '0027': '1', '0028': '2', '0029': '3',
    '0030': '4', '0031': '5', '0032': '6', '0033': '7', '0034': '8', '0035': '9',
    '0036': 'plus', '0037': 'horizontal_line', '0038': 'slash',
    '0039': 'paranthesis_left', '0040': 'paranthesis_right', '0041': 'sqrt', '0042': 'sqrt'}

label_dict = {label: 0 for label in set(letter_dict.values())}

def ExtractSet(path, save_path, name=None):
    page = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
    kernel = np.ones((4, 4), np.uint8)
    blur = cv2.GaussianBlur(page, (3, 3), 1)
    _, thresh = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)

    dilation = cv2.dilate(thresh, kernel, iterations=2)
    dilation_2 = cv2.dilate(thresh, kernel=np.ones((1, 1), np.uint8))  # Optional, may vary per dataset

    contours, _ = cv2.findContours(dilation, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    for index, contour in enumerate(contours):
        mask = np.zeros_like(dilation)
        cv2.drawContours(mask, [contour], -1, 255, thickness=cv2.FILLED)
        MaskedLetter = cv2.bitwise_and(dilation_2, dilation_2, mask=mask)

        if name is not None:
            index = label_dict[name]
            label_dict[name] = index + 1

        x, y, w, h = cv2.boundingRect(contour)
        max_edge = max(h, w)
        blank = np.zeros((max_edge, max_edge), np.uint8)
        

        x1, x2 = int((max_edge - h) / 2), int((max_edge + h) / 2)
        y1, y2 = int((max_edge - w) / 2), int((max_edge + w) / 2)

        blank[x1:x2, y1:y2] = MaskedLetter[y:y + h, x:x + w]
        blank = cv2.copyMakeBorder(blank, 5,5,5,5, borderType=cv2.BORDER_CONSTANT, value=0)  
        
        letter = cv2.resize(blank, (32,32))

        cv2.imwrite(save_path + f"\\{index}.jpg", letter)

path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\ender"

save_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\dataset\ender\1"

for file in os.listdir(path):
    img_path = os.path.join(path,file)
    if img_path.split(".")[-1] == "png":
        ExtractSet(img_path,save_path=save_path,name="1")
