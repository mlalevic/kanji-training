using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Drawing;
using System.Xml;

namespace JapaneseFonts
{
    public class KanjiCharacterDefinition : CharacterDefinition
    {
        public int lessonNumber{get;set;}
        public KanjiCharacterDefinition(int index, string character, string text, int lesson)
            : base(index, character, text)
        {
            this.lessonNumber = lesson;
        }
    }

    public class KanjiFormat : IFileDefinition, ICharacterExtractor //for heisig-data.txt
    {

        string[] lines;

        #region ICharacterExtractor Members

        public CharacterDefinition Extract(string definition)
        {
            string[] items = definition.Split(new char[] { ':' }, StringSplitOptions.RemoveEmptyEntries);
            return new KanjiCharacterDefinition(
                int.Parse(items[0]),
                items[1],
                items[4],
                int.Parse(items[7]));
        }

        #endregion

        #region IFileDefinition Members

        public void Load(string fileName)
        {
            using (StreamReader sr = new StreamReader(fileName, Encoding.UTF8))
            {
                lines = sr.ReadToEnd().Split(new char[]{'\n'}, StringSplitOptions.RemoveEmptyEntries);
            }
        }

        #endregion

        #region IEnumerable<string> Members

        public IEnumerator<string> GetEnumerator()
        {
            foreach (string line in lines)
            {
                if (line.StartsWith("#")) continue;
                yield return line;
            }
        }

        #endregion

        #region IEnumerable Members

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return this.GetEnumerator();
        }

        #endregion
    }

    public static class OutputFormat
    {

		
        public static string GetString(CharacterList characters, SortedDictionary<int, Rectangle> measures)
        {
            StringBuilder sb = new StringBuilder();
            foreach (var c in characters)
            {
                if (!measures.ContainsKey(c.index))
                {
                    sb.AppendLine("*********" + c.text);
                    continue;
                }

                var m = measures[c.index];
                
                sb.AppendFormat("{0}[{1},{2},{3},{4}]", c.text, m.X, m.Y, m.Width, m.Height);
                sb.AppendLine();
            }
            return sb.ToString();
        }
    }
}
