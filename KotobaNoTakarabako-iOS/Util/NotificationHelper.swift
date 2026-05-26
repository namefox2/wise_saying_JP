import UserNotifications
import Foundation

class NotificationHelper {
    static let shared = NotificationHelper()
    private init() {}

    func requestPermission() async -> Bool {
        do {
            return try await UNUserNotificationCenter.current()
                .requestAuthorization(options: [.alert, .sound, .badge])
        } catch {
            return false
        }
    }

    func scheduleDaily(hour: Int, minute: Int) {
        let center = UNUserNotificationCenter.current()
        center.removePendingNotificationRequests(withIdentifiers: ["dailyStudy"])

        var components = DateComponents()
        components.hour   = hour
        components.minute = minute

        let content      = UNMutableNotificationContent()
        content.title    = "言葉の宝箱"
        content.body     = "오늘의 일본어 학습을 시작해보세요! 📚"
        content.sound    = .default

        let trigger = UNCalendarNotificationTrigger(dateMatching: components, repeats: true)
        let request = UNNotificationRequest(identifier: "dailyStudy", content: content, trigger: trigger)
        center.add(request)
    }

    func cancelAll() {
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
    }
}
