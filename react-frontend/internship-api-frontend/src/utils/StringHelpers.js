import noImagePlaceholder from '../assets/no_image_placeholder.png';
const getImageSrc = (base64) => {
    if (!base64) return noImagePlaceholder;
    return `data:image/jpeg;base64,${base64}`;
  };

export {getImageSrc};

const getMimeTypeFromBase64 = (base64) => {
  if (base64.startsWith('/9j')) return 'image/jpeg';
  if (base64.startsWith('iVBOR')) return 'image/png';
  if (base64.startsWith('R0lGOD')) return 'image/gif';
  
  return 'image/png'; // default fallback
}
export {getMimeTypeFromBase64};