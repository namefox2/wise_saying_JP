import Foundation
import Observation

@Observable
class WordRepository {
    static let shared = WordRepository()

    private(set) var allWords: [Word] = []
    private(set) var isLoading = false

    private init() { loadAll() }

    private func loadAll() {
        let levels = ["n1", "n2", "n3", "n4", "n5"]
        allWords = levels.flatMap { level -> [Word] in
            guard let url = Bundle.main.url(forResource: "words_\(level)", withExtension: "json", subdirectory: "data"),
                  let data = try? Data(contentsOf: url),
                  let words = try? JSONDecoder().decode([Word].self, from: data)
            else { return [] }
            return words
        }
    }

    func words(for level: String) -> [Word] {
        level == "전체" ? allWords : allWords.filter { $0.level == level }
    }

    func refresh(baseURL: String) async {
        isLoading = true
        defer { isLoading = false }
        let levels = ["n1", "n2", "n3", "n4", "n5"]
        var refreshed: [Word] = []
        for level in levels {
            guard let url = URL(string: "\(baseURL)/words_\(level).json"),
                  let (data, _) = try? await URLSession.shared.data(from: url),
                  let words = try? JSONDecoder().decode([Word].self, from: data)
            else { continue }
            refreshed.append(contentsOf: words)
            let cache = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first?
                .appendingPathComponent("words_\(level).json")
            if let cache { try? data.write(to: cache) }
        }
        if !refreshed.isEmpty { allWords = refreshed }
    }
}
