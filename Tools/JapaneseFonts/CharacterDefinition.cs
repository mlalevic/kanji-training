using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace JapaneseFonts
{
    public class CharacterDefinition
    {
        public int index { get; set; }
        public string character { get; set; }
        public string text { get; set; }

        public CharacterDefinition(int index, string character, string text)
        {
            this.index = index;
            this.character = character;
            this.text = text;
        }

    }

    public interface ICharacterExtractor
    {
        CharacterDefinition Extract(string definition);
    }

    public interface IFileDefinition : IEnumerable<string>
    {
        void Load(string fileName);
    }

    public class CharacterList : IEnumerable<CharacterDefinition>
    {
        private SortedDictionary<int, CharacterDefinition> characterList;
        private ICharacterExtractor extractor;
        private IFileDefinition file;

        public CharacterList(ICharacterExtractor extractor, IFileDefinition file)
        {
            this.extractor = extractor;
            this.file = file;
            this.characterList = new SortedDictionary<int, CharacterDefinition>();
        }

        public void Load(string fileName)
        {
            file.Load(fileName);
            foreach(string next in file)
            {
                if (next == null) continue;
                var character = extractor.Extract(next);
                if (character == null) continue;
                characterList.Add(character.index, character);
            }
        }

        public int Count { get { return characterList.Count; } }


        #region IEnumerable<CharacterDefinition> Members

        public IEnumerator<CharacterDefinition> GetEnumerator()
        {
            foreach (CharacterDefinition c in characterList.Values)
            {
                yield return c;
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
}
