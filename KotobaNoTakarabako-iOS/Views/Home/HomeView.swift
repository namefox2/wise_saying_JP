import SwiftUI

struct HomeView: View {
    @Environment(\.appColors) var colors
    @Environment(ThemeManager.self)  var themeManager
    @State private var vm = HomeViewModel()
    @State private var quoteDestination: String? = nil
    @State private var jlptDestination:  String? = nil

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 20) {
                    header
                    if let q = vm.todayQuote { todayCard(q) }
                    tabSwitcher
                    if vm.selectedTab == 0 { categoryGrid } else { jlptGrid }
                }
                .padding(.horizontal)
                .padding(.bottom, 20)
            }
            .background(colors.background.ignoresSafeArea())
            .navigationDestination(isPresented: Binding(
                get: { quoteDestination != nil },
                set: { if !$0 { quoteDestination = nil } }
            )) {
                if let cat = quoteDestination { QuoteCardView(category: cat) }
            }
            .navigationDestination(isPresented: Binding(
                get: { jlptDestination != nil },
                set: { if !$0 { jlptDestination = nil } }
            )) {
                if let level = jlptDestination { JlptView(initialLevel: level) }
            }
        }
    }

    // MARK: - Subviews

    private var header: some View {
        HStack {
            VStack(alignment: .leading, spacing: 2) {
                Text("言葉の宝箱")
                    .font(.title.bold())
                    .foregroundColor(colors.accent)
                Text("일본어 학습")
                    .font(.caption)
                    .foregroundColor(colors.textTertiary)
            }
            Spacer()
            Circle().fill(Color.green).frame(width: 10, height: 10)
        }
        .padding(.top, 8)
    }

    private func todayCard(_ quote: Quote) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text("오늘의 명언")
                    .font(.caption.bold())
                    .foregroundColor(colors.accent)
                    .padding(.horizontal, 8).padding(.vertical, 4)
                    .background(colors.accent.opacity(0.15))
                    .clipShape(Capsule())
                Spacer()
                Text(Date(), style: .date)
                    .font(.caption2)
                    .foregroundColor(colors.textTertiary)
            }
            Text(quote.kanji)
                .font(.system(size: 20, weight: .medium))
                .foregroundColor(colors.text)
                .lineLimit(3)
            Text(quote.korean)
                .font(.system(size: 14))
                .foregroundColor(colors.textSecondary)
                .lineLimit(2)
            HStack {
                Spacer()
                Text("— \(quote.author)")
                    .font(.caption)
                    .foregroundColor(colors.textTertiary)
            }
        }
        .padding(16)
        .background(colors.cardBg)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .shadow(color: colors.shadow, radius: 4, y: 2)
        .onTapGesture { quoteDestination = quote.cat }
    }

    private var tabSwitcher: some View {
        HStack(spacing: 0) {
            ForEach(["명언 카테고리", "JLPT 레벨"], id: \.self) { label in
                let idx = label == "명언 카테고리" ? 0 : 1
                Button { vm.selectedTab = idx } label: {
                    Text(label)
                        .font(.subheadline.bold())
                        .foregroundColor(vm.selectedTab == idx ? colors.accent : colors.textTertiary)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                        .background(vm.selectedTab == idx ? colors.accent.opacity(0.12) : Color.clear)
                }
            }
        }
        .background(colors.surface)
        .clipShape(RoundedRectangle(cornerRadius: 10))
    }

    private var categoryGrid: some View {
        let cols = [GridItem(.flexible()), GridItem(.flexible())]
        return LazyVGrid(columns: cols, spacing: 12) {
            ForEach(vm.quoteCategories, id: \.self) { cat in
                Button { quoteDestination = cat } label: {
                    HStack {
                        Text(catIcon(cat))
                        Text(cat)
                            .font(.subheadline.bold())
                            .foregroundColor(colors.text)
                        Spacer()
                    }
                    .padding(14)
                    .background(colors.surface)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                }
            }
        }
    }

    private var jlptGrid: some View {
        let cols = [GridItem(.flexible()), GridItem(.flexible())]
        return LazyVGrid(columns: cols, spacing: 12) {
            ForEach(vm.jlptLevels, id: \.self) { level in
                Button { jlptDestination = level } label: {
                    HStack {
                        Circle()
                            .fill(levelColor(level))
                            .frame(width: 10, height: 10)
                        Text(level)
                            .font(.subheadline.bold())
                            .foregroundColor(colors.text)
                        Spacer()
                    }
                    .padding(14)
                    .background(colors.surface)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                }
            }
        }
    }

    // MARK: - Helpers

    private func catIcon(_ cat: String) -> String {
        switch cat {
        case "전체": return "📖"
        case "노력": return "💪"
        case "성공": return "🏆"
        case "사랑": return "❤️"
        case "인생": return "🌸"
        case "학습": return "📚"
        case "마음": return "🧘"
        case "속담": return "🏮"
        default:     return "✨"
        }
    }

    private func levelColor(_ level: String) -> Color {
        switch level {
        case "N1": return .red
        case "N2": return .orange
        case "N3": return .yellow
        case "N4": return .green
        case "N5": return .blue
        default:   return colors.textTertiary
        }
    }
}
