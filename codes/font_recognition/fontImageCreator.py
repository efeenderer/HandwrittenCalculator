from PIL import Image, ImageDraw, ImageFont
import os

font_path = r"E:\Python_Projeler\ComputerVisionProjects\FinalProject\codes\font_recognition\fonts"



letters_digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/!"

size = 512
font_size = 200
image_size = [size,size]


for font_name in os.listdir(font_path):
    if font_name.split(".")[-1].lower() != "ttf":
        continue
        
    for char in letters_digits:
        font = ImageFont.truetype(os.path.join(font_path,font_name), font_size)
            
        img = Image.new(mode="RGB", size=(size,size),color=(255,255,255))
        
        draw = ImageDraw.Draw(img)

        try:
            bbox = draw.textbbox((0, 0), char, font=font)
            text_width = bbox[2] - bbox[0]
            text_height = bbox[3] - bbox[1]

        
            scale_factor = min(image_size[0] / text_width, image_size[1] / text_height)
            new_font_size = int(font_size * scale_factor) 
            font = ImageFont.truetype(os.path.join(font_path,font_name), new_font_size)  # Yeni font boyutunu uygula

            # Yeni bbox'u tekrar hesapla
            bbox = draw.textbbox((0, 0), char, font=font)
            text_width = bbox[2] - bbox[0]
            text_height = bbox[3] - bbox[1]

            # Ortalamak için pozisyon hesapla
            x = (image_size[0] - text_width) // 2 - bbox[0]
            y = (image_size[1] - text_height) // 2 - bbox[1]

            # Metni çiz
            draw.text((x, y), char, font=font, fill="black")

            save_path = os.path.join(font_path,font_name.split(".")[-2])
            os.makedirs(save_path,exist_ok=True)

            img.save(os.path.join(save_path, f"{char}.png"))
        except Exception as e:
            print(f"Error: {e}     \ncharacter: {char} ")