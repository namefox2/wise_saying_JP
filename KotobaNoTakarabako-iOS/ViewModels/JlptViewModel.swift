import SwiftUI
import Observation

@Observable
class JlptViewModel {
    var selectedLevel: String = "전체"
    var currentIndex: Int = 0

    let levels = ["전체", "N1", "N2", "N3", "N4", "N5"]

    var filteredWords: [Word] { WordRepository.shared.words(for: selectedLevel) }
    var currentWord: Word?  { filteredWords.isEmpty ? nil : filteredWords[currentIndex] }
    var progress: String    { "\(currentIndex + 1) / \(filteredWords.count)" }

    func next() {
        guard !filteredWords.isEmpty else { return }
        currentIndex = (currentIndex + 1) % filteredWords.count
    }

    func previous() {
        guard !filteredWords.isEmpty else { return }
        currentIndex = (currentIndex - 1 + filteredWords.count) % filteredWords.count
    }

    func selectLevel(_ level: String) {
        selectedLevel = level
        currentIndex  = 0
    }
}
