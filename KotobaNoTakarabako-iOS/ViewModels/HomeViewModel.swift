import SwiftUI
import Observation

@Observable
class HomeViewModel {
    var selectedTab: Int = 0   // 0: 명언 카테고리 | 1: JLPT 레벨
    var todayQuote: Quote?

    let quoteCategories = ["전체", "노력", "성공", "사랑", "인생", "학습", "마음", "속담", "기타"]
    let jlptLevels      = ["전체", "N1", "N2", "N3", "N4", "N5"]

    init() { todayQuote = QuoteRepository.shared.todayQuote() }
    func refresh() { todayQuote = QuoteRepository.shared.todayQuote() }
}
