import React, { useState } from 'react';
import { Navbar } from './components/Navbar';
import { Hero } from './components/Hero';
import { ProgressBar } from './components/ProgressBar';
import { AboutStats } from './components/AboutStats';
import { ServicesGrid } from './components/ServicesGrid';
import { WhyChooseUs } from './components/WhyChooseUs';
import { Process } from './components/Process';
import { Testimonials } from './components/Testimonials';
import { PhotoCtaBanner } from './components/PhotoCtaBanner';
import { Faq } from './components/Faq';
import { FinalCta } from './components/FinalCta';
import { Footer } from './components/Footer';
import { BookingModal } from './components/BookingModal';

export const App: React.FC = () => {
  const [isBookingOpen, setIsBookingOpen] = useState(false);
  const [selectedService, setSelectedService] = useState('Dental Check-Up');

  const handleOpenBooking = () => {
    setSelectedService('Dental Check-Up');
    setIsBookingOpen(true);
  };

  const handleSelectService = (serviceTitle: string) => {
    setSelectedService(serviceTitle === 'All Services' ? 'Dental Check-Up' : serviceTitle);
    setIsBookingOpen(true);
  };

  return (
    <div className="min-h-screen bg-[#060C14] text-slate-100 font-['Plus_Jakarta_Sans'] flex flex-col selection:bg-[#0BB8B8] selection:text-white">
      {/* 01 — Navbar */}
      <Navbar onOpenBooking={handleOpenBooking} />

      <main className="flex-1">
        {/* 02 — Hero */}
        <Hero onOpenBooking={handleOpenBooking} />

        {/* 03 — Progress Steps Bar */}
        <ProgressBar />

        {/* 04 — About / Stats */}
        <AboutStats />

        {/* 05 — Services Grid */}
        <ServicesGrid onSelectService={handleSelectService} />

        {/* 06 — Why Choose Us */}
        <WhyChooseUs onOpenBooking={handleOpenBooking} />

        {/* 07 — Process (How It Works) */}
        <Process />

        {/* 08 — Testimonials */}
        <Testimonials />

        {/* 09 — Full-Width Photo CTA Banner */}
        <PhotoCtaBanner onOpenBooking={handleOpenBooking} />

        {/* 10 — FAQ */}
        <Faq />

        {/* 11 — Final CTA */}
        <FinalCta onOpenBooking={handleOpenBooking} />
      </main>

      {/* 12 — Footer */}
      <Footer />

      {/* Interactive Quick Booking Modal */}
      <BookingModal
        isOpen={isBookingOpen}
        onClose={() => setIsBookingOpen(false)}
        initialService={selectedService}
      />
    </div>
  );
};

export default App;
