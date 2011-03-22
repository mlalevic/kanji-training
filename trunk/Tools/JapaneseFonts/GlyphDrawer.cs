using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace JapaneseFonts
{
    public class GlyphDrawer
    {
        //private const int MaxWidth = 2400;
		private const int MaxWidth = 300;
        //private const int MaxHeight = 5000;
		private const int MaxHeight = 40000;
        private const int FontSize = 40;
        public bool ranOut { get; private set; }
        string fontFamily;
        public Bitmap img { get; private set; }
        public SortedDictionary<int, Rectangle> areas { get; private set; }

        public GlyphDrawer(string fontFamily)
        {
            this.fontFamily = fontFamily;
            this.ranOut = false;
            areas = new SortedDictionary<int, Rectangle>();
        }

        int Ceiling(float val)
        {
            return (int)Math.Ceiling((double)val);
        }

        public void Draw(CharacterList list)
        {
            areas.Clear();
            img = new Bitmap(MaxWidth, MaxHeight);
            img.MakeTransparent(Color.White);
            Graphics g = Graphics.FromImage(img);
            g.FillRectangle(Brushes.White, new Rectangle(0, 0, MaxWidth, MaxHeight));


            Font f = new Font(fontFamily, FontSize);

            int x = 0;
            int y = 0;
            int maxY = 0;

            foreach (var i in list)
            {
                var s = i.character;
                var size = g.MeasureString(s, f);
				size = new SizeF(size.Width + 20, size.Height + 7);

                if (x + Ceiling(size.Width) > MaxWidth)
                {
                    x = 0;
                    y += maxY;
                    maxY = 0;
                    if (y > MaxHeight)
                    {
                        ranOut = true;
                        return;
                    }
                }
                g.DrawString(s, f, Brushes.Black, x + 10, y + 3);
                areas.Add(i.index, new Rectangle(x, y, Ceiling(size.Width), Ceiling(size.Height)));
                x += Ceiling(size.Width);
                maxY = maxY >= Ceiling(size.Height) ? maxY : Ceiling(size.Height);
            }
            g.Dispose();
            f.Dispose();
            //cuttoff if greater
            y += maxY;
            if (y < MaxHeight)
            {
                var oldImage = img;
                img = new Bitmap(MaxWidth, y);
                var newg = Graphics.FromImage(img);
                newg.DrawImage(oldImage, 0, 0);
                newg.Dispose();
                oldImage.Dispose();
            }
        }
    }
}
