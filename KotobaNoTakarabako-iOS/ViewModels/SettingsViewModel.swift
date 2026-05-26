import SwiftUI
import Observation

@Observable
class SettingsViewModel {
    var isRefreshing = false
    var refreshMessage: String?

    private let refreshBaseURL = "https://raw.githubusercontent.com/namefox2/wise_saying_JP/main/assets/data"

    func refreshData() async {
        isRefreshing = true
        defer { isRefreshing = false }
        await QuoteRepository.shared.refresh(from: "\(refreshBaseURL)/quotes.json")
        await WordRepository.shared.refresh(baseURL: refreshBaseURL)
        refreshMessage = "데이터가 업데이트되었습니다."
    }

    func applyNotification(enabled: Bool, hour: Int, minute: Int) {
        Task {
            if enabled {
                let granted = await NotificationHelper.shared.requestPermission()
                if granted { NotificationHelper.shared.scheduleDaily(hour: hour, minute: minute) }
            } else {
                NotificationHelper.shared.cancelAll()
            }
        }
    }
}
