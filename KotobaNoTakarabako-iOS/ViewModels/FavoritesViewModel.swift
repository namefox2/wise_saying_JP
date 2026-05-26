import SwiftUI
import Observation

@Observable
class FavoritesViewModel {
    var selectedTab: Int = 0   // 0: 명언 | 1: 단어

    func favoriteQuotes(from items: [FavoriteItem]) -> [Quote] {
        let ids = Set(items.filter { $0.type == "quote" }.map(\.itemId))
        return QuoteRepository.shared.quotes.filter { ids.contains($0.id) }
    }

    func favoriteWords(from items: [FavoriteItem]) -> [Word] {
        let ids = Set(items.filter { $0.type == "word" }.map(\.itemId))
        return WordRepository.shared.allWords.filter { ids.contains($0.id) }
    }

    func exportText(quotes: [Quote], words: [Word]) -> String {
        var text = "=== 즐겨찾기 명언 ===\n\n"
        for q in quotes { text += "[\(q.cat)] \(q.kanji)\n\(q.korean)\n— \(q.author)\n\n" }
        text += "=== 즐겨찾기 단어 ===\n\n"
        for w in words  { text += "[\(w.level)] \(w.kanji) (\(w.reading)) : \(w.meaning)\n" }
        return text
    }
}
