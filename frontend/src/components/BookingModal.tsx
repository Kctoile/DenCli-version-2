import React, { useState } from 'react';
import { X, Calendar, User, Phone, CheckCircle, ArrowRight } from 'lucide-react';

interface BookingModalProps {
  isOpen: boolean;
  onClose: () => void;
  initialService?: string;
}

export const BookingModal: React.FC<BookingModalProps> = ({
  isOpen,
  onClose,
  initialService = 'Dental Check-Up'
}) => {
  const [formData, setFormData] = useState({
    fullName: '',
    phone: '',
    service: initialService,
    preferredDate: '',
    notes: ''
  });
  const [isSubmitted, setIsSubmitted] = useState(false);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitted(true);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-[#060C14]/80 backdrop-blur-sm animate-fade-in">
      <div className="relative w-full max-w-lg bg-white rounded-3xl shadow-2xl border border-[#DDE4EE] p-6 sm:p-8 overflow-hidden">
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-5 right-5 p-2 rounded-full text-slate-400 hover:text-slate-700 hover:bg-slate-100 transition-colors"
          aria-label="Close modal"
        >
          <X className="w-5 h-5" />
        </button>

        {!isSubmitted ? (
          <div>
            <div className="mb-6">
              <span className="text-[0.65rem] tracking-[0.2em] font-bold text-[#0BB8B8] uppercase">
                DIRECT CLINICAL RESERVATION
              </span>
              <h3 className="font-extrabold text-2xl text-[#0A1520] mt-1">
                Book Your Dental Visit
              </h3>
              <p className="text-[0.82rem] text-[#7A8BA0] mt-1">
                Instant confirmation. No waiting line. 100% gentle care.
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-[0.75rem] font-semibold text-slate-700 mb-1">
                  Full Name *
                </label>
                <div className="relative">
                  <User className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                  <input
                    type="text"
                    required
                    placeholder="Nguyễn Văn A"
                    value={formData.fullName}
                    onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                    className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-[#0BB8B8] focus:ring-1 focus:ring-[#0BB8B8]"
                  />
                </div>
              </div>

              <div>
                <label className="block text-[0.75rem] font-semibold text-slate-700 mb-1">
                  Phone Number *
                </label>
                <div className="relative">
                  <Phone className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                  <input
                    type="tel"
                    required
                    placeholder="0901 234 567"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-[#0BB8B8] focus:ring-1 focus:ring-[#0BB8B8]"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-[0.75rem] font-semibold text-slate-700 mb-1">
                    Select Service
                  </label>
                  <select
                    value={formData.service}
                    onChange={(e) => setFormData({ ...formData, service: e.target.value })}
                    className="w-full px-3.5 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-[#0BB8B8]"
                  >
                    <option value="Dental Check-Up">Dental Check-Up ($80+)</option>
                    <option value="Teeth Cleaning">Teeth Cleaning ($120+)</option>
                    <option value="Tooth Whitening">Tooth Whitening ($299+)</option>
                    <option value="Dental Implants">Dental Implants ($1,800+)</option>
                    <option value="Veneers & Crowns">Veneers & Crowns ($650+)</option>
                    <option value="Emergency Care">Emergency Care (Priority)</option>
                  </select>
                </div>

                <div>
                  <label className="block text-[0.75rem] font-semibold text-slate-700 mb-1">
                    Preferred Date
                  </label>
                  <div className="relative">
                    <Calendar className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                    <input
                      type="date"
                      value={formData.preferredDate}
                      onChange={(e) => setFormData({ ...formData, preferredDate: e.target.value })}
                      className="w-full pl-10 pr-3.5 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-[#0BB8B8]"
                    />
                  </div>
                </div>
              </div>

              <div>
                <label className="block text-[0.75rem] font-semibold text-slate-700 mb-1">
                  Symptoms or Special Request
                </label>
                <textarea
                  rows={2}
                  placeholder="E.g. Tooth sensitivity, consultation for ceramic veneer..."
                  value={formData.notes}
                  onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                  className="w-full px-3.5 py-2 rounded-xl border border-slate-200 text-sm focus:outline-none focus:border-[#0BB8B8]"
                />
              </div>

              <div className="pt-2">
                <button
                  type="submit"
                  className="w-full inline-flex items-center justify-center gap-2 h-12 rounded-full text-sm font-bold text-white bg-[#0BB8B8] hover:bg-[#099E9E] shadow-xl shadow-[#0BB8B8]/25 transition-all active:scale-[0.98]"
                >
                  <span>Confirm Appointment</span>
                  <ArrowRight className="w-4 h-4" />
                </button>
              </div>

              <div className="text-center pt-1">
                <a
                  href="book"
                  className="text-xs text-[#0BB8B8] hover:underline font-medium"
                >
                  Already have an account? Open Full Patient Booking Portal →
                </a>
              </div>
            </form>
          </div>
        ) : (
          <div className="text-center py-8">
            <div className="w-16 h-16 rounded-full bg-[#EBF9F9] text-[#0BB8B8] flex items-center justify-center mx-auto mb-4">
              <CheckCircle className="w-8 h-8" />
            </div>
            <h3 className="font-extrabold text-2xl text-[#0A1520]">
              Appointment Requested!
            </h3>
            <p className="text-sm text-[#7A8BA0] mt-2 max-w-sm mx-auto">
              Thank you, <strong>{formData.fullName}</strong>. Our clinical coordinator will call{' '}
              <strong>{formData.phone}</strong> within 15 minutes to confirm your time slot.
            </p>
            <div className="mt-8 flex justify-center gap-3">
              <button
                onClick={() => {
                  setIsSubmitted(false);
                  onClose();
                }}
                className="h-10 px-8 rounded-full text-xs font-bold text-white bg-[#0A1520] hover:bg-[#1A2838]"
              >
                Close
              </button>
              <a
                href="login"
                className="inline-flex items-center justify-center h-10 px-6 rounded-full text-xs font-bold text-[#0BB8B8] bg-[#EBF9F9] hover:bg-[#d8f5f5]"
              >
                Go to Patient Portal
              </a>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};
