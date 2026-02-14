import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Hls from 'hls.js';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    const [movies, setMovies] = useState([]);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const videoRef = useRef(null);
    const hlsRef = useRef(null);

    // Backend-dən filmləri çəkmək
    useEffect(() => {
        axios.get('http://localhost:8081/api/v1/movies')
            .then(res => {
                const data = res.data.content || res.data;
                setMovies(Array.isArray(data) ? data : []);
            })
            .catch(err => console.error("Backend bağlantı xətası:", err));
    }, []);

    // Video pleyer tənzimləmələri
    useEffect(() => {
        if (selectedMovie && selectedMovie.videoUrl && videoRef.current) {
            setIsLoading(true);

            if (hlsRef.current) {
                hlsRef.current.destroy();
            }

            let finalUrl = selectedMovie.videoUrl;
            if (!finalUrl.includes('/movies/') && finalUrl.includes('/movie-videos/')) {
                finalUrl = finalUrl.replace('/movie-videos/', '/movie-videos/movies/');
            }

            console.log("Pleyer bu linkə müraciət edir:", finalUrl);

            if (Hls.isSupported()) {
                const hls = new Hls({
                    enableWorker: true,
                    xhrSetup: function (xhr) {
                        xhr.withCredentials = false;
                    }
                });

                hls.loadSource(finalUrl);
                hls.attachMedia(videoRef.current);

                hls.on(Hls.Events.MANIFEST_PARSED, () => {
                    setIsLoading(false);
                    videoRef.current.play().catch(() => console.log("Auto-play gözlənir..."));
                });

                hls.on(Hls.Events.ERROR, (event, data) => {
                    if (data.fatal) {
                        setIsLoading(false);
                        if(data.details === "manifestLoadError") {
                            alert("Video tapılmadı. Zəhmət olmasa MinIO bağlantısını yoxlayın.");
                        }
                    }
                });

                hlsRef.current = hls;
            } else if (videoRef.current.canPlayType('application/vnd.apple.mpegurl')) {
                videoRef.current.src = finalUrl;
            }
        }

        return () => {
            if (hlsRef.current) hlsRef.current.destroy();
        };
    }, [selectedMovie]);

    return (
        <div style={{ backgroundColor: '#0a0a0a', minHeight: '100vh', color: 'white', fontFamily: 'Segoe UI, sans-serif' }}>

            {/* Navbar - NEO CINEMA */}
            <nav className="navbar navbar-dark bg-black px-4 shadow-lg mb-4 border-bottom border-danger border-opacity-25">
                <span className="navbar-brand fw-bold text-danger fs-2" style={{ letterSpacing: '3px', textShadow: '2px 2px 4px rgba(220, 53, 69, 0.3)' }}>
                    NEO CINEMA
                </span>
            </nav>

            <div className="container pb-5">
                {!selectedMovie ? (
                    /* Film Siyahısı */
                    <div className="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4 animate__animated animate__fadeIn">
                        {movies.map(movie => (
                            <div className="col" key={movie.id}>
                                <div className="card bg-transparent text-white border-0 h-100" style={{ cursor: 'pointer' }} onClick={() => setSelectedMovie(movie)}>
                                    <div className="position-relative overflow-hidden rounded-3 shadow-lg" style={{ height: '380px', border: '1px solid #222' }}>
                                        <img
                                            src={movie.coverImageUrl || 'https://placehold.co/400x600/111/white?text=Neo+Cinema'}
                                            className="w-100 h-100"
                                            style={{ objectFit: 'cover', transition: 'transform 0.5s ease' }}
                                            onMouseOver={(e) => e.target.style.transform = 'scale(1.08)'}
                                            onMouseOut={(e) => e.target.style.transform = 'scale(1)'}
                                            alt={movie.title}
                                        />
                                        <div className="position-absolute bottom-0 start-0 w-100 p-3" style={{ background: 'linear-gradient(to top, rgba(0,0,0,0.9), transparent)' }}>
                                            <h6 className="mb-0 text-truncate fw-bold">{movie.title}</h6>
                                            <small className="text-danger">{movie.releaseDate?.substring(0, 4)}</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    /* Video Pleyer Sahəsi */
                    <div className="animate__animated animate__fadeIn">
                        <button className="btn btn-outline-light btn-sm mb-4 rounded-pill px-3" onClick={() => setSelectedMovie(null)}>
                            ← Geri qayıt
                        </button>

                        <div className="position-relative ratio ratio-16x9 bg-black rounded-4 shadow-2xl border border-secondary border-opacity-25 mx-auto overflow-hidden" style={{ maxWidth: '1100px' }}>
                            {isLoading && (
                                <div className="position-absolute top-50 start-50 translate-middle text-center" style={{zIndex: 10}}>
                                    <div className="spinner-border text-danger" style={{ width: '3rem', height: '3rem' }} role="status"></div>
                                    <p className="mt-3 text-uppercase fw-light" style={{ letterSpacing: '2px' }}>Neo Cinema təqdim edir...</p>
                                </div>
                            )}
                            <video ref={videoRef} controls className="w-100 h-100" poster={selectedMovie.coverImageUrl} />
                        </div>

                        <div className="mt-5 mx-auto" style={{ maxWidth: '1100px' }}>
                            <div className="d-flex justify-content-between align-items-start">
                                <div>
                                    <h1 className="fw-bold mb-2">{selectedMovie.title}</h1>
                                    <p className="text-danger fw-bold">{selectedMovie.genres?.map(g => g.name).join(' • ')}</p>
                                </div>
                                <div className="text-end">
                                    <span className="badge bg-danger fs-5 px-3 py-2 rounded-pill shadow">IMDb {selectedMovie.averageRating || 'N/A'}</span>
                                </div>
                            </div>
                            <p className="text-secondary mt-3 lh-lg" style={{ fontSize: '1.15rem', maxWidth: '800px' }}>{selectedMovie.description}</p>

                            <div className="row mt-5 py-4 border-top border-secondary border-opacity-25 text-muted">
                                <div className="col-md-4 mb-2"><strong>BURAXILIŞ İLİ:</strong> <span className="text-white ms-2">{selectedMovie.releaseDate?.substring(0, 4)}</span></div>
                                <div className="col-md-4 mb-2"><strong>MÜDDƏT:</strong> <span className="text-white ms-2">{selectedMovie.duration} dəq</span></div>
                                <div className="col-md-4 mb-2 text-md-end"><strong>STATUS:</strong> <span className="text-success ms-2">4K Ultra HD</span></div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default App;