from PIL import Image
save_dir = r"D:\Download\6.15\ch11\src\main\resources\static"
img = Image.new('RGB', (32, 32), color=(0, 128, 255))  # 蓝色方块
img.save(f"{save_dir}/favicon.ico", format='ICO', sizes=[(32, 32)])