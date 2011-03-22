using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Drawing.Imaging;
using System.IO;

namespace JapaneseFonts
{
	class MainClass
	{
		static private CharacterList list;
        static private SortedDictionary<int, Rectangle> areas;

		
		public static void Main(string[] args)
		{
			//DrawPngKanji();
			SplitKanji();
		}
		
		private static void DrawPngKanji(){
			KanjiFormat format = new KanjiFormat();
            list = new CharacterList(format, format);
            list.Load("kanji.txt");

            GlyphDrawer drawer = new GlyphDrawer("Ume Gothic");

            drawer.Draw(list);


            drawer.img.Save("kanji.png", ImageFormat.Png);
            areas = drawer.areas;
            using (StreamWriter sw = new StreamWriter("kanjidef.txt"))
            {
                sw.Write(OutputFormat.GetString(list, drawer.areas));
            }

		}
		
		private static void SplitKanji(){
            var kanji = Bitmap.FromFile("kanji.png");
            int rowsSplit = 9;
            var sb = new StringBuilder();
            var prevY = 0;
            var currentRow = 0;
            var currentY = 0;
            var imageCount = 0;
            var characterCount = 0;
            var previousCharacterCount = 0;
            using (StreamReader sr = new StreamReader("kanjidef.txt"))
            {
                var lines = sr.ReadToEnd().Split(new string[]{"\n"}, StringSplitOptions.RemoveEmptyEntries);
                foreach (var l in lines)
                {
                    characterCount++;
                    var y = int.Parse(l.Split('[')[1].Split(',')[1]);
                    if (currentY != y)
                    {
                        currentRow++;
                        if (currentRow < rowsSplit)
                        {
                            currentY = y;
                            continue;
                        }
                        
                        currentRow = 0;
                        
                        imageCount++;
                        sb.AppendFormat("{0} {1}-{2} {3}", prevY, previousCharacterCount, characterCount, GetImageName(imageCount));
                        sb.AppendLine();
                        previousCharacterCount = characterCount + 1;

                        var height = currentY - prevY;
                        prevY = currentY;
                        currentY = y;

                        SaveImage(kanji, kanji.Width, height, prevY, imageCount);
                    }
                }

                if (kanji.Height != prevY)
                {
                    imageCount++;
                    sb.AppendFormat("{0} {1}-{2} {3}", prevY, previousCharacterCount, characterCount, GetImageName(imageCount));
                    sb.AppendLine();
                    var height = kanji.Height - prevY;
                    SaveImage(kanji, kanji.Width, height, kanji.Height, imageCount);
                }

                using (StreamWriter sw = new StreamWriter("imgdef.txt"))
                {
                    sw.Write(sb.ToString());
                }
            }
        }

        private static string GetImageName(int imageCount)
        {
            return "kanji" + imageCount.ToString() + ".png";
        }

        private static void SaveImage(Image kanji, int width, int height, int y, int imageCount)
        {
            using(Bitmap b = new Bitmap(width, height))
            {
                using(Graphics g = Graphics.FromImage(b)){
                    var dest = new Rectangle(0, 0, width, height);
                    var source = new Rectangle(0, y - height, width, height);
                    g.DrawImage(kanji, dest, source, GraphicsUnit.Pixel);
                    b.Save(GetImageName(imageCount), ImageFormat.Png);
                }
            }
        }
    }
}